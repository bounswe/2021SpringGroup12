<template>
  <Layout>
    <h2 class="mb-8 text-4xl font-bold text-center capitalize">
      Path: <span class="text-green-700">{{ header(section) }}</span>
    </h2>
    <ResponseFilter v-model="section" :fetch="fetchResponse" />
    <ResponseList v-if="!loading && !error" :posts="posts" />
    <!-- Start of loading animation -->
    <div class="mt-40" v-if="loading">
      <p class="text-6xl font-bold text-center text-gray-500 animate-pulse">
        Loading...
      </p>
    </div>
    <!-- End of loading animation -->

    <!-- Start of error alert -->
    <div class="mt-12 bg-red-50" v-if="error">
      <h3 class="px-4 py-1 text-4xl font-bold text-white bg-red-800">
        {{ error.title }}
      </h3>
      <p class="p-4 text-lg font-bold text-red-900">{{ error.message }}</p>
    </div>
    <!-- End of error alert -->
  </Layout>
</template>

<script>
import axios from "axios"
import Layout from "../components/Layout.vue"
import ResponseFilter from "../components/UserResponseFilter.vue"
import ResponseList from "../components/ResponseList.vue"

export default {
  components: {
    Layout,
    ResponseFilter,
    ResponseList,
  },
  data() {
    return {
      section: [],
      posts: [],
      loading: false,
      error: null,
    }
  },
  methods: {
    extractImage(post) {
      const defaultImg = {
        url: "http://placehold.it/210x140?text=N/A",
        caption: post.title,
      }
      return defaultImg
    },
    header(value) {
      if (!value) return ""
      value = value.toString()
      return value
    },
    async fetchResponse() {
      const possible_paths = [
        "/siders" /* + */,
        "/drugs" /* + */,
        "/users" /* + */,
        "/prots" /*  */,
        "/reactions" /*  */,
        "/articles" /* + */,
        "/points" /* + */,
      ]

      const headers = {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
      }

      try {
        this.error = null
        this.loading = true

        if (possible_paths.some(v => this.section.includes(v))) {
          console.log(this.section)
          const response = axios
            .get("http://127.0.0.1:8000" + this.section + "/", { headers })
            .then(value => {
              if (this.section == "/siders/drugs") {
                /*possible options:
                  #15 "/siders/{umls_cui}/drugs", + 
                */
                this.posts = value.data.items.map(post => ({
                  drugbank_id: post.drugbank_id,
                  drug_name: post.drug_name,
                  smiles: post.smiles,
                  description: post.description,
                }))
              } else if (this.section.startsWith("/drugs")) {
                /*possible options:
                  #8 "/drugs/plus"+
                  #9 "/drugs/{drug_id}/interactions",+
                  #10 "/drugs/{drug_id}/side_effects",+
                  #11 "/drugs/{drug_id}/targets",+
                  #13 "/drugs/same_protein",+
                  #16 "/drugs/keyword/{keyword}",+
                */
                if (this.section == "/drugs/plus") {
                  console.log(value.data)
                  this.posts = value.data.items.map(post => ({
                    drugbank_id: post.drugbank_id,
                    drug_name: post.drug_name,
                    smiles: post.smiles,
                    description: post.description,
                    side_effect_names: post.side_effect_names,
                    target_names: post.target_names,
                  }))
                } else if (this.section.endsWith("/interactions")) {
                  console.log(value.data)
                  this.posts = value.data.items.map(post => ({
                    drugbank_id: post.drugbank_id,
                    drug_name: post.drug_name,
                  }))
                } else if (this.section.endsWith("/side_effects")) {
                  console.log(value.data)
                  this.posts = value.data.items.map(post => ({
                    umls_cui: post.umls_cui,
                    side_effect_name: post.side_effect_name,
                  }))
                } else if (this.section.endsWith("/targets")) {
                  console.log(value.data)
                  this.posts = value.data.items.map(post => ({
                    uniprot_id: post.uniprot_id,
                    target_name: post.target_name,
                    sequence: post.sequence,
                  }))
                  console.log(this.posts)
                } else if (this.section.endsWith("/same_protein")) {
                  console.log(value.data)
                  this.posts = value.data.items.map(post => ({
                    uniprot_id: post.uniprot_id,
                    drugbank_ids: post.drugbank_ids,
                  }))
                } else if (this.section.startsWith("/drugs/keyword/")) {
                  this.posts = value.data.items.map(post => ({
                    drugbank_id: post.drugbank_id,
                    drug_name: post.drug_name,
                    smiles: post.smiles,
                    description: post.description,
                  }))
                  console.log(this.posts)
                }
              } else if (this.section.startsWith("/users")) {
                this.posts = value.data.items.map(post => ({
                  username: post.username,
                  institution: post.institution,
                }))
              } else if (this.section.startsWith("/prots")) {
                /* possible options:
                    #12 "/prots/{uniprot_id}/drugs",+
                    #14 "/prots/same_drugs", + 
                    #17 "/prots/{uniprot_id}/least_effecting_drug", TODO
                */
                if (this.section.endsWith("/drugs")) {
                  this.posts = value.data.items.map(post => ({
                    drugbank_id: post.drugbank_id,
                    drug_name: post.drug_name,
                    smiles: post.smiles,
                    description: post.description,
                  }))
                } else if (this.section == "/prots/same_drugs") {
                  this.posts = value.data.items.map(post => ({
                    drugbank_id: post.drugbank_id,
                    uniprot_ids: post.uniprot_ids,
                  }))
                } else if (this.section.endsWith("/least_effecting_drug")) {
                  //TODO
                }
              } else if (this.section.startsWith("/reactions")) {
                // TODO
              } else if (this.section.startsWith("/article")) {
                this.posts = value.data.items.map(post => ({
                  doi: post.doi,
                  contributors: post.contributors,
                }))
              } else if (this.section.startsWith("/points")) {
                this.posts = value.data.items.map(post => ({
                  institution: post.institution,
                  point: post.point,
                }))
              } else {
                console.log(value.data)
              }
            })
            .catch(reason => {
              this.posts = [
                {
                  status: reason.response.status,
                  statusText: reason.response.statusText,
                  detail: reason.response.data.detail,
                },
              ]
            })
        }
      } catch (err) {
        if (err.response) {
          // client received an error response (5xx, 4xx)
          this.error = {
            title: "Server Response",
            message: err.message,
          }
        } else if (err.request) {
          // client never received a response, or request never left
          this.error = {
            title: "Unable to Reach Server",
            message: err.message,
          }
        } else {
          // There's probably an error in your code
          this.error = {
            title: "Application Error",
            message: err.message,
          }
        }
      }
      this.loading = false
    },
  },
  mounted() {
    this.fetchResponse()
  },
}
</script>
