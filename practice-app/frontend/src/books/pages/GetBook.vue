<template>
  <Layout>
<<<<<<< HEAD
    <ResponseFilter v-model="section" :fetch="fetchResponse" />
    <ResponseList v-if="!loading && !error" :posts="posts" />
    <!-- Start of loading animation -->
    <div class="mt-40" v-if="loading">
      <p class="text-6xl font-bold text-center text-gray-500 animate-pulse">
        Loading...
      </p>
    </div>
    <!-- End of loading animation -->
=======
    <form v-if="!sent" class="postForm" @submit.prevent="getBooks">
      <p>
        Author Name
        <label>
          <input type="text" placeholder="required" v-model="name" />
        </label>
      </p>
      <p>
        Max Result
        <label>
          <input type="text" placeholder="optional" v-model="max_results" />
        </label>
      </p>
      <button type="submit">Get Books</button>
    </form>

    <div class="mt-5" v-if="end && success">
      <table border="1px solid black">
        <tr>
          <th>Number</th>
          <th>Title</th>
          <th>Author</th>
          <th>URL</th>
          <th>Publication Date</th>
          <th>Summary</th>
          <th>uuid</th>
          <th>uri</th>
          <th>Isbn13</th>
        </tr>

        <tr v-for="(book, index) in books">
          <td>{{ index + 1 }}</td>
          <td>{{ book.book_title }}</td>
          <td>{{ book.book_author }}</td>
          <td>
            <a :href="book.url">{{ book.url }}</a>
          </td>
          <td>{{ book.publication_dt }}</td>
          <td>{{ book.summary }}</td>
          <td>{{ book.uuid }}</td>
          <td>{{ book.uri }}</td>
          <td>{{ book.isbn13 }}</td>
        </tr>
      </table>
    </div>
>>>>>>> 82ae232e71606912b38e247e84aba32f5eeed0af

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
<<<<<<< HEAD
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
=======
export default {
  components: {
    Layout,
  },
  data() {
    return {
      error: null,
      success: false,
      end: false,
      books: [],
      name: "",
      max_results: "",
    };
  },
  methods: {
    async getBooks() {
>>>>>>> 82ae232e71606912b38e247e84aba32f5eeed0af
      const headers = {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
      };
<<<<<<< HEAD

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
=======
      try {
        this.end = false;
        this.error = null;
        var url = ""
        if(this.max_results == ""){
         url = `http://127.0.0.1:5000/books/?name=${this.name}`;
        }else {
          url = `http://127.0.0.1:5000/books/?name=${this.name}&max_results=${this.max_results}`;
        }
        const response = await axios.get(url, { headers });
        if(response.data.num_results == 0){
          this.error = {
            title: "No result",
            message: "Couldn't find any book of this author."
          }
        }else if(response.data.num_results > 0){
          this.success = true
        }
        this.data = response.data;
        this.books = response.data.books.map((book) => ({
          book_title: book.book_title,
          book_author: book.book_author,
          url: book.url,
          publication_dt: book.publication_dt,
          summary: book.summary,
          uuid: book.uuid,
          uri: book.uri,
          isbn13: book.isbn13,
        }));
>>>>>>> 82ae232e71606912b38e247e84aba32f5eeed0af
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
<<<<<<< HEAD
          // There's probably an error in your code
=======
          // There's probably an error in the code
>>>>>>> 82ae232e71606912b38e247e84aba32f5eeed0af
          this.error = {
            title: "Application Error",
            message: err.message,
          };
        }
      }
<<<<<<< HEAD
      this.loading = false;
    },
  },
  mounted() {
    this.fetchResponse();
  },
};
</script>
=======
      this.end = true;
    },
  },
};
</script>
>>>>>>> 82ae232e71606912b38e247e84aba32f5eeed0af
