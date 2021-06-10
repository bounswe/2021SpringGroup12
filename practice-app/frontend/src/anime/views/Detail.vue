<template>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
  <p class="error" v-if="this.searchError !==''">{{ this.searchError }}</p>
  <form id="searchBar" @submit.prevent="search">
    <input type="text" v-model="searchQuery">
    <button type="submit"><i class="fa fa-search"></i></button>
  </form>
  <div class="anime">
    <AnimeDetails
      v-if="!loading && !error"
      @clicked-related="goToAnime"
      :anime="this.anime"
    />
    <h1 v-if="loading">Loading</h1>
    <div class="errormsg" v-if="error">
      <h1>{{error.message}}</h1>
      <button @click="fetchAnime">Try Again</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import AnimeDetails from '../components/AnimeDetails.vue';

export default {
  components: {
    AnimeDetails,
  },
  data() {
    return {
      anime: {},
      loading: false,
      error: null,
      searchQuery: '',
      searchError: null,
    };
  },
  methods: {
    async fetchAnime() {
      try {
        this.error = null;
        this.loading = true;
        const url = `http://${process.env.VUE_APP_API_URL}/anime/${this.$route.params.id}`;
        const response = await axios.get(url);
        const anime = response.data;
        this.anime = {
          title: anime.title,
          id: anime.mal_id,
          episodes: anime.episodes,
          image: anime.image,
          airing: anime.airing,
          start_date: anime.start_date
            ? new Date(anime.start_date).toLocaleDateString('en-US', {
              year: 'numeric',
              month: 'short',
            })
            : null,
          end_date: anime.end_date
            ? new Date(anime.end_date).toLocaleDateString('en-US', {
              year: 'numeric',
              month: 'short',
            })
            : null,
          score: anime.score,
          rating: anime.rating,
          type: anime.type,
          synopsis: anime.synopsis,
          duration: anime.duration,
          sequel: anime.sequel,
          prequel: anime.prequel,
          genres: anime.genres,
        };
      } catch (err) {
        this.error = {
          title: 'error',
          message: err.response.data.msg,
        };
      }
      this.loading = false;
    },
    goToAnime(value) {
      this.$router.push({ name: 'Anime', params: { id: value } });
    },
    search() {
      if (this.searchQuery.length >= 3) {
        this.$router.push({ name: 'Search', params: { query: this.searchQuery } });
      }

      if (this.searchQuery.length < 3) {
        this.searchError = 'Search query must be at least 3 characters long!';
      }
    },
  },
  watch: {
    $route(to) {
      if (to.name === 'Anime') {
        this.fetchAnime();
      }
    },
  },
  mounted() {
    this.fetchAnime();
  },
};
</script>
