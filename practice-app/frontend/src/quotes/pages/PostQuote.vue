
<template>
  <div class="login">
    <b><mark> Post Quote</mark></b
    ><br />
    <form v-if="!sent" class="postForm" @submit.prevent="postAddUser">
      <p>ID</p>
      <label>
        <input
          type="text"
          placeholder="required"
          v-model="user_body._id"
        />
      </label>
      <p>Author</p>
      <label>
        <input
          type="text"
          placeholder="required"
          v-model="user_body.quoteAuthor"
        />
      </label>
      <p>Genre</p>
      <input type="text" placeholder="required" v-model="user_body.quoteGenre" />
      <p>Text</p>
      <input type="text" placeholder="required" v-model="user_body.quoteText" />

      <br /><br />
      <br />
      <button type="submit">Post</button>
    </form>

    <h1 v-if="success">{{ this.response }}</h1>
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
        _id: "",
        quoteAuthor: "",
        quoteGenre: "",
        quoteText: "",
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
      //const url = `http://127.0.0.1:5000/addQuotes/`;
      const url = `http://localhost:5000/addQuotes/`;

      const response = await axios
        .post(url, this.user_body)
        .then((value) => {
          console.log(value);
          if (value.status == 200) {
            this.response = value.data;
            this.success = true;
            this.fail = false;
          }
        })
        .catch((value) => {
          console.log(value.response);
          console.log(value.response.status);
          console.log(value.status);
          console.log(value.data);
          console.log(value.response.data);
          this.success = false;
          this.fail = true;
          this.error = value.response.data;
        });
      console.log(response);

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