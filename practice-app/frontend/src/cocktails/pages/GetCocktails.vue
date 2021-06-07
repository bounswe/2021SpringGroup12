<template>
  <Layout>
    <form v-if="!sent" class="postForm" @submit.prevent="getCocktails">
      <p>Get Cocktails</p>
      <label>
        <input
          type="text"
          placeholder="required"
          v-model="cocktail_type"
        />
      </label>
      <button type="submit">Get Cocktails</button>
    </form>

    

    <div class="mt-5" v-if="end">
      <table border= "1px solid black">
        <tr>
          <th>Cocktail Name</th>
          <th>Ingredient 1</th>
          <th>Ingredient 2</th>
          <th>Ingredient 3</th>
          <th>Ingredient 4</th>
          <th>Glass</th>
          <th>Instructions</th>
        </tr>

      <tr  v-for="(cocktail) in data">
        <td>{{ cocktail.Cocktail_name }}</td>
        <td>{{ cocktail.Ingredient_1 }}</td>
        <td>{{ cocktail.Ingredient_2 }}</td>
        <td>{{ cocktail.Ingredient_3 }}</td>
        <td>{{ cocktail.Ingredient_4 }}</td>
        <td>{{ cocktail.Glass }}</td>
        <td>{{ cocktail.Instructions }}</td>
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
      cocktail_type : ""
    };
  },
  methods: {
    extractImage(cocktail) {
      const defaultImg = {
        url: "http://placehold.it/210x140?text=N/A",
        caption: cocktail.description,
      };
      return defaultImg;
    },
    header(value) {
      if (!value) return "";
      value = value.toString();
      return value;
    },
    async getCocktails() {
      const headers = {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
      };

      try {
        this.end = false;
        this.error = null;
        const url = `http://127.0.0.1:5000/cocktails/get_cocktails/?cocktail_name=${this.cocktail_type}`;
        const response = await axios.get(url,{ headers });
        this.data = response.data['cocktails'];
        for (let i = 0; i < this.data.length; i++) {
          let cocktail = this.data[i]
          this.data[i] = {
                Cocktail_name: cocktail.cocktail_name,
                Ingredient_1: cocktail.ingredient_1,
                Ingredient_2: cocktail.ingredient_2,
                Ingredient_3: cocktail.ingredient_3,
                Ingredient_4: cocktail.ingredient_4,
                Glass: cocktail.glass,
                Instructions: cocktail.instructions,
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