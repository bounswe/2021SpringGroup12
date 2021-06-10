<template>
  <Layout>
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
      success: false,
      end: false,
      books: [],
      name: "",
      max_results: "",
    };
  },
  methods: {
    async getBooks() {
      const headers = {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
      };
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
          // There's probably an error in the code
          this.error = {
            title: "Application Error",
            message: err.message,
          };
        }
      }
      this.end = true;
    },
  },
};
</script>