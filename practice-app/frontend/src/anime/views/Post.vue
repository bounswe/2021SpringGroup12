<template>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
  <p class="error" v-if="this.searchError !==''">{{ this.searchError }}</p>
  <form id="searchBar" @submit.prevent="search">
    <input type="text" v-model="searchQuery" />
    <button type="submit"><i class="fa fa-search"></i></button>
  </form>
  <div class="anime">
    <form v-if="!sent" class="postForm" @submit.prevent="postAnime">
      <p>Title*</p>
      <input type="text" v-model="body.title" placeholder="Enter a string"/>
      <p>Image URL*</p>
      <input type="text" v-model.number="body.image" placeholder="Enter a string"/>
      <p>Episodes*</p>
      <input type="text" v-model.number="body.episodes" placeholder="Enter a integer"/>
      <p>Is airing*</p>
      <input type="checkbox" v-model="body.airing"/>
      <p>Start Date</p>
      <input type="date" v-model="body.start_date" />
      <p>End Date</p>
      <input type="date" v-model="body.end_date" />
      <p>Score*</p>
      <input type="text" v-model.number="body.score" placeholder="Enter a float"/>
      <p>Rating*</p>
      <select v-model="body.rating">
        <option>G</option>
        <option>PG</option>
        <option>PG-13</option>
        <option>R</option>
        <option>R+</option>
        <option>Rx</option>
      </select>
      <p>Type*</p>
      <select v-model="body.type">
        <option>TV</option>
        <option>OVA</option>
        <option>Movie</option>
        <option>Special</option>
        <option>ONA</option>
        <option>Music</option>
      </select>
      <p>Synopsis*</p>
      <textarea v-model="body.synopsis" cols="30" rows="10" placeholder="Enter a string"></textarea>
      <p></p>
      <button type="submit">Post</button>
    </form>
    <h1 v-if="success">Success</h1>
    <h1 v-if="fail">Fail</h1>
    <p v-if="fail && this.error.data.err==='Validation'" v-for="(error, index) in this.error.data.errs" :key="index">{{ error.field}}:{{ error.msg }}</p>
    <p v-if="fail && this.error.data.err!=='Validation'">{{this.error.data}}</p>
    <button v-if="sent" @click="goHome">Go home</button>
    <button v-if="sent" @click="postAgain">Post again</button>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      sent: false,
      success: false,
      fail: false,
      error: null,
      searchQuery: '',
      searchError: null,
      body: {
        title: null,
        episodes: null,
        id: null,
        image: null,
        airing: false,
        start_date: null,
        end_date: null,
        score: null,
        rating: null,
        type: null,
        synopsis: null,
      },
    };
  },
  methods: {
    async postAnime() {
      try {
        this.sent = false;
        this.success = false;
        this.fail = false;
        this.error = null;

        this.body.start_date = (new Date(this.body.start_date)).toISOString();
        this.body.end_date = (new Date(this.body.end_date)).toISOString();

        const url = `${process.env.VUE_APP_API_URL}/anime/`;
        await axios.post(url, this.body);
        this.success = true;
        this.fail = false;
      } catch (err) {
        this.success = false;
        this.fail = true;
        this.error = err.response;
      }
      this.sent = true;
    },
    search() {
      if (this.searchQuery.length >= 3) {
        this.$router.push({ name: 'Search', params: { query: this.searchQuery } });
      }

      if (this.searchQuery.length < 3) {
        this.searchError = 'Search query must be at least 3 characters long!';
      }
    },
    goHome() {
      this.$router.push({ name: 'Animehome' });
    },
    postAgain() {
      this.$router.go();
    }
  },
};
</script>

<style>
.postForm {
  display: block;
}
</style>
