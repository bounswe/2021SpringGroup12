<template>
  <link
    rel="stylesheet"
    href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"
  />
  <p class="error" v-if="this.searchError !== ''">{{ this.searchError }}</p>
  <form id="searchBar" @submit.prevent="search">
    <input type="text" v-model="searchQuery" />
    <button type="submit"><i class="fa fa-search"></i></button>
  </form>
  <div class="anime">
    <AnimeList
      @clicked="renderDetails"
      v-if="!loading && !error"
      :animes="animes"
    />
    <p v-if="loading">Loading</p>
  </div>
</template>

<script>
import axios from 'axios';
import AnimeList from '../components/AnimeList.vue';

export default {
  components: {
    AnimeList,
  },
  data() {
    return {
      animes: [],
      loading: false,
      error: null,
      searchQuery: '',
      searchError: null,
    };
  },
  methods: {
    async fetchAnimes() {
      try {
        this.error = null;
        this.loading = true;
        const url = `${process.env.VUE_APP_API_URL}/anime/search?query=${this.$route.params.query}&limit=15`;
        const response = await axios.get(url);
        const animes = response.data;
        this.animes = animes.map((anime) => ({
          title: anime.title,
          image: anime.image,
          synopsis: anime.synopsis,
          type: anime.type,
          start_date: false
            ? new Date(anime.start_date).toLocaleDateString('en-US', {
              year: 'numeric',
              month: 'short',
            })
            : null,
          end_date: false
            ? new Date(anime.end_date).toLocaleDateString('en-US', {
              year: 'numeric',
              month: 'short',
            })
            : null,
          score: anime.score,
          rating: anime.rating,
          airing: anime.airing,
          id: anime.mal_id,
        }));
      } catch (err) {
        this.error = {
          title: 'error',
          message: err.msg,
        };
      }
      this.loading = false;
    },
    renderDetails(value) {
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
      if (to.name === 'Search') {
        this.fetchAnimes();
      }
    },
  },
  mounted() {
    this.fetchAnimes();
  },
};
</script>
