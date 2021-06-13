<template>
  <Layout>
    <ResponseFilter v-model="section" :fetch="fetchResponse" />
    <ResponseList v-if="!loading && !error" :posts="posts" />
    <form v-if="!sent" class="postForm" @submit.prevent="getMovies">
      <p>Keyword</p>
      <label>
        <input
          type="text"
          placeholder="Enter a keyword"
          v-model="keyword"
        />
      </label>
      <button type="submit">Show Movies</button>
    </form>
<!--    <ResponseList v-if="!loading && !error" :issues="issues" />-->

    <div class="mt-5" v-if="end">
      <table border= "1px solid black">
        <tr>
          <th>Title</th>
          <th>Mpaa_Rating</th>
          <th>Critics_Pick</th>
          <th>Reviewer</th>
          <th>Headline</th>
          <th>Short_Summary</th>
          <th>link</th>
        </tr>

      <tr  v-for="(movies, index) in data">
        <td>{{ movies.display_title }}</td>
        <td>{{ movies.mpaa_rating }}</td>
        <td>{{ movies.critics_pick }}</td>
        <td>{{ movies.byline }}</td>
        <td>{{ movies.headline }}</td>
        <td>{{ movies.summary_short }}</td>
        <td>{{ movies.link }}</td>
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
    };
  },
  methods: {
    header(value) {
      if (!value) return "";
      value = value.toString();
      return value;
    },
    async getMovies() {
      const headers = {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
      };

      try {
        this.end = false;
        this.error = null;
        const url = `http://${process.env.VUE_APP_API_URL}/movies/?keyword=${this.keyword}`;
        const response = await axios.get(url,{ headers });
        this.data = response.data;
        for (let i = 0; i < this.data.length; i++) {
          let issue = this.data[i]
          console.log(data[i])
          this.data[i] = {
                Title:  movies.display_title,
                Mpaa_Rating :movies.mpaa_rating, 
                Critics_Pick: movies.critics_pick,
                Reviewer: movies.byline,
                Headline: movies.headline,
                Short_Summary: movies.summary_short,
                link : movies.summary_short
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
