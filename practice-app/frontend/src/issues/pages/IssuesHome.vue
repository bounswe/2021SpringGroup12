<template>
  <div id="nav">
    <router-link to="/getall">Get Issues </router-link> |
    <router-link to="/getissue">Get A Issue </router-link> |
    <router-link to="/postissues">Post A Issue </router-link>
  </div>
  <div class="get issues">
    <form v-if="!download" class="postForm" @submit.prevent="downloadIssues">
      <p>State</p>
      <select v-model="state">
        <option value="open">
          Open
        </option>
        <option value="closed">
          Closed
        </option>
        <option value="all">
          All
        </option>
      </select>
      <br />
      <button type="submit">Download Issues</button>
    </form>
  </div>
  <div class="mt-12 bg-red-50" v-if="download && !error">
      <h3 class="px-4 py-1 text-4xl font-bold text-white bg-red-800">
        {{ response }}
      </h3>
    </div>
  <div class="mt-12 bg-red-50" v-if="error">
      An Error Occurred While Downloading Issues!
    </div>
  <router-view/>
</template>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
</style>

<script>
import axios from "axios";

export default {
  data() {
    return {
      download: false,
      state: "open",
      error: '',
      response : ""
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
    async downloadIssues() {
      const headers = {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
      };

      try {
        this.error = null;
        const url = `http://${process.env.VUE_APP_API_URL}/download_issues?state=${this.state}`;
        const response = await axios.get(url,{ headers });
        this.response = response.data;
        this.download = true

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
    },
  }
};
</script>