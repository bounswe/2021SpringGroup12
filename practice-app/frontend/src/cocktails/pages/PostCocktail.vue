
<template>
  <div class="login">
    <b><mark> Post Cocktail</mark></b
    ><br />
    <form v-if="!sent" class="postForm" @submit.prevent="postAddUser">
      <p>Cocktail Name</p>
      <label>
        <input
          type="text"
          placeholder="required"
          v-model="user_body.strDrink"
        />
      </label>
      <p>Ingredient 1</p>
      <label>
        <input
          type="text"
          placeholder="required"
          v-model="user_body.strIngredient1"
        />
      </label>
      <p>Ingredient 2</p>
      <input type="text" placeholder="required" v-model="user_body.strIngredient2" />
      <p>Ingredient 3</p>
      <input type="text" placeholder="required" v-model="user_body.strIngredient3" />
      <p>Ingredient 4</p>
      <input type="text" placeholder="required" v-model="user_body.strIngredient4" />
      <p>Glass</p>
      <input type="text" placeholder="required" v-model="user_body.strGlass" />
      <p>Instructions</p>
      <input type="text" placeholder="required" v-model="user_body.strInstructions" />
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
        strDrink: "",
        strIngredient1: "",
        strIngredient2: "",
        strIngredient3: "",
        strIngredient4: "",
        strGlass: "",
        strInstructions: "",
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
      const url = `http://${process.env.VUE_APP_API_URL}/cocktails/create_cocktail/`;


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