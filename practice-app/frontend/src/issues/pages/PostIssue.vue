
<template>
  <div class="login">
    <b><mark> Post Books</mark></b
    ><br />
    <form v-if="!sent" class="postForm" @submit.prevent="postAddUser">
      <p>Number</p>
      <label>
        <input
          type="text"
          placeholder="required"
          v-model="user_body.number"
        />
      </label>
      <p>Assignees</p>
      <label>
        <input
          type="text"
          placeholder="comma seperated"
          v-model="user_body.assignees"
        />
      </label>
      <p>Description</p>
      <input type="text" v-model="user_body.description" />
      <p>Labels</p>
      <input type="text" placeholder="comma seperated" v-model="user_body.labels" />
      <p>State</p>
      <select v-model="user_body.state">
        <option value="open">
          Open
        </option>
        <option value="closed">
          Closed
        </option>
      </select>
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
        number: 0,
        assignees: [],
        description: "",
        labels: [],
        state: "",
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
      const url = `http://${process.env.VUE_APP_API_URL}/issues`;
      if (!this.user_body.assignees.length) {
        this.user_body.assignees = [];
      } else {
        this.user_body.assignees = this.user_body.assignees.split(",");
      }
      if (!this.user_body.labels.length) {
        this.user_body.labels = [];
      } else {
        this.user_body.labels = this.user_body.labels.split(",");
      }

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
