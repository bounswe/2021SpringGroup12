<template>
  <Layout>
    <form v-if="!sent" class="postForm" @submit.prevent="getIssues">
      <p>Issue Number</p>
      <label>
        <input
          type="text"
          placeholder="required"
          v-model="issue_number"
        />
      </label>
      <button type="submit">Get Issues</button>
    </form>
<!--    <ResponseList v-if="!loading && !error" :issues="issues" />-->

    <div class="ml-1" v-if="end">
      <li>Number = {{data.number}}</li>
      <li>Assignees = {{data.assignees}}</li>
      <li>Description = {{data.description}}</li>
      <li>Labels = {{data.labels}}</li>
      <li>State = {{data.state}}</li>
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

export default {
  components: {
    Layout,
  },
  data() {
    return {
      error: null,
      end: false,
      data: [],
      issue_number : 1
    };
  },
  methods: {
    extractImage(issue) {
      const defaultImg = {
        url: "http://placehold.it/210x140?text=N/A",
        caption: issue.description,
      };
      return defaultImg;
    },
    header(value) {
      if (!value) return "";
      value = value.toString();
      return value;
    },
    async getIssues() {
      const headers = {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
      };

      try {
        this.end = false;
        this.error = null;
        const url = `http://127.0.0.1:5000/issues/${this.issue_number}`;
        const response = await axios.get(url,{ headers });
        this.data = response.data;
        for (let i = 0; i < this.data.length; i++) {
          let issue = this.data[i]
          this.data[i] = {
                Number: issue.number,
                Assignees: issue.assignees,
                Description: issue.description,
                Labels: issue.labels,
                State: issue.state
              };
        }

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
      this.end = true;
    },
  }
};
</script>
