<template>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
  <p class="error" v-if="this.searchError !==''">{{ this.searchError }}</p>
  <form id="searchBar" @submit.prevent="search">
    <input type="text" v-model="searchQuery" />
    <button type="submit"><i class="fa fa-search"></i></button>
  </form>
  <div class="anime">
    <form v-if="!sent" class="postForm" @submit.prevent="postAnime">
      <p>Title</p>
      <input type="text" v-model="body.title" />
      <p>Episodes</p>
      <input type="text" v-model.number="body.episodes" />
      <p>Id</p>
      <input type="text" v-model.number="body.id" />
      <p>Is airing</p>
      <input type="checkbox" v-model="body.airing" />
      <p>Start Date</p>
      <input type="date" v-model="body.start_date" />
      <p>End Date</p>
      <input type="date" v-model="body.end_date" />
      <p>Score</p>
      <input type="text" v-model.number="body.score" />
      <p>Rating</p>
      <select v-model="body.rating">
        <option>G</option>
        <option>PG</option>
        <option>PG-13</option>
        <option>R</option>
        <option>R+</option>
        <option>Rx</option>
      </select>
      <p>Type</p>
      <select v-model="body.type">
        <option>TV</option>
        <option>OVA</option>
        <option>Movie</option>
        <option>Special</option>
        <option>ONA</option>
        <option>Music</option>
      </select>
      <p>Synopsis</p>
      <textarea v-model="body.synopsis" cols="30" rows="10"></textarea>
      <p></p>
      <button type="submit">Post</button>
    </form>
    <h1 v-if="success">Success</h1>
    <h1 v-if="fail">Fail - {{ this.error }}</h1>
    <button v-if="sent" @click="goHome">Go home</button>
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
        title: '',
        episodes: 0,
        id: 0,
        image_url: '',
        airing: false,
        start_date: new Date(),
        end_date: new Date(),
        score: 0,
        rating: 'None',
        type: 'None',
        synopsis: '',
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
        console.log(err);
        this.success = false;
        this.fail = true;
        this.error = err.response.data.msg;
      }
      this.sent = true;
    },
    search() {
      if (this.searchQuery.length >= 3) {
        console.log(this.searchQuery);
        this.$router.push({ name: 'Search', params: { query: this.searchQuery } });
      }

      if (this.searchQuery.length < 3) {
        this.searchError = 'Search query must be at least 3 characters long!';
      }
    },
    goHome() {
      this.$router.push({ name: 'Animehome' });
    },
  },
};
</script>

<style>
.postForm {
  display: block;
}
</style>
