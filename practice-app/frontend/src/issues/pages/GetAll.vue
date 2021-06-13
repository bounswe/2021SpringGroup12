<template>
  <Layout>
    <form v-if="!sent" class="postForm" @submit.prevent="getIssues">
      <p>Max Issue</p>
      <label>
        <input
          type="text"
          placeholder="required"
          v-model="max_results"
        />
      </label>
      <button type="submit">Get Issues</button>
    </form>
<!--    <ResponseList v-if="!loading && !error" :issues="issues" />-->

    <div class="mt-5" v-if="end">
      <table border= "1px solid black">
        <tr>
          <th>Index</th>
          <th>Number</th>
          <th>Assignees</th>
          <th>Description</th>
          <th>Labels</th>
          <th>State</th>
        </tr>

      <tr  v-for="(issue, index) in data">
        <td>{{ index }}</td>
        <td>{{ issue.Number }}</td>
        <td>{{ issue.Assignees }}</td>
        <td>{{ issue.Description }}</td>
        <td>{{ issue.Labels }}</td>
        <td>{{ issue.State }}</td>
      </tr>
        </table>
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
      max_results : 30
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
        const url = `http://${process.env.VUE_APP_API_URL}/issues?max_results=${this.max_results}`;
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
