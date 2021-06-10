
<template>
  <div class="login">
    <h2 v-if="!sent"><mark> Post Books</mark></h2
    ><br />
    <form v-if="!sent" class="postForm" @submit.prevent="postAddUser">
      <p>Author Name</p>
      <input
        type="text"
        placeholder="required"
        v-model="user_body.book_author"
      />
      <p>Book Title</p>
      <input
        type="text"
        placeholder="required"
        v-model="user_body.book_title"
      />
      <p>url</p>
      <textarea type="text" v-model="user_body.url" />
      <p>Publication Date</p>
      <input
        type="text"
        placeholder="year-month-day"
        v-model="user_body.publication_dt"
      />
      <p>Summary</p>
      <textarea type="text" v-model="user_body.summary" />
      <p>UUID</p>
      <input type="text" v-model="user_body.uuid" />
      <p>Uri</p>
      <input type="text" v-model="user_body.uri" />
      <p>ISBN13</p>
      <textarea
        type="text"
        placeholder="comma seperated"
        v-model="user_body.isbn13s"
      />
      <br /><br />
      <br />
      <button type="submit">Post</button>
    </form>

    <h1 v-if="success">{{ this.response }}</h1>
    <h3 v-if="success">
      <a href="http://localhost:8080/postbooks">Post another book</a>
    </h3>
    <h1 v-if="fail">{{ this.error }}</h1>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      sent: false,
      success: false,
      fail: false,
      response: "",
      error: null,
      searchQuery: "",
      searchError: null,
      user_body: {
        book_author: "",
        book_title: "",
        url: "",
        publication_dt: "",
        summary: "",
        uuid: "",
        uri: "",
        isbn13s: "",
        isbn13: [],
      },
    };
  },
  methods: {
    async postAddUser() {
      this.sent = false;
      this.success = false;
      this.response = "";
      this.fail = false;
      this.error = null;
      const url = `http://127.0.0.1:5000/books/`;
      //change isbn numbers from comma seperated string to Array
      if (!this.user_body.isbn13s.length) {
        this.user_body.isbn13 = [];
      } else {
        this.user_body.isbn13 = this.user_body.isbn13s.split(",");
      }
      // check whether required fields are empty
      if (
        !this.user_body.book_author.replace(" ", "").length ||
        !this.user_body.book_title.replace(" ", "").length
      ) {
        this.success = false;
        this.fail = true;
        this.error = "Please fill required fields!";
      } else {
        const response = await axios
          .post(url, this.user_body)
          .then((value) => {
            if (value.status == 200) {
              this.response = value.data;
              this.success = true;
              this.fail = false;
            }
          })
          .catch((value) => {
            this.success = false;
            this.fail = true;
            this.error = value.response.data;
          });
      }
      if (this.success) {
        this.sent = true;
      }
    },
  },
};
</script>

<style>
.postForm {
  display: block;
}
</style>
