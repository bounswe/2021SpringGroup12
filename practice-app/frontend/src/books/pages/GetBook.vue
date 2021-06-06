<template>
  <Layout>
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
import axios from "axios";
import Layout from "../components/Layout.vue";
import ResponseFilter from "../components/UserResponseFilter.vue";
import ResponseList from "../components/ResponseList.vue";

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
    };
  },
  methods: {
    extractImage(post) {
      const defaultImg = {
        url: "http://placehold.it/210x140?text=N/A",
        caption: post.title,
      };
      return defaultImg;
    },
    header(value) {
      if (!value) return "";
      value = value.toString();
      return value;
    },
    async fetchResponse() {
      const headers = {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
      };

      try {
        this.error = null;
        this.loading = true;

        console.log(this.section);
        const response = axios
          .get("http://127.0.0.1:5000/books/?" + this.section, { headers })
          .then((value) => {

            if (value.data.num_results == 0) {
              this.posts = [{
                no_result:"This author does not have any book in the system.",  
              }];
            } else {
              this.posts = value.data.books.map((post) => ({
                Author: post.book_author,
                Title: post.book_title,
                Publication_Date: post.publication_dt,
                Summary: post.summary,
                Uuid: post.uuid,
                Uri: post.uri,
                Isbn13: post.isbn13,
              }));
            }
          })
          .catch((reason) => {
            console.log(reason);
            this.posts = [
              {
                status: reason.response.status,
                statusText: reason.response.statusText,
                detail: reason.response.data,
              },
            ];
          });
      } catch (err) {
        if (err.response) {
          // client received an error response (5xx, 4xx)
          this.error = {
            title: "Server Response",
            message: err.message,
          };
        } else if (err.request) {
          // client never received a response, or request never left
          this.error = {
            title: "Unable to Reach Server",
            message: err.message,
          };
        } else {
          // There's probably an error in your code
          this.error = {
            title: "Application Error",
            message: err.message,
          };
        }
      }
      this.loading = false;
    },
  },
  mounted() {
    this.fetchResponse();
  },
};
</script>
