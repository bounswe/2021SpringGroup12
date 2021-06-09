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
<<<<<<< HEAD
        console.log(response);
        const { animes } = response.data;
        this.animes = animes.map((anime) => ({
          title: anime.title,
          image: anime.image_url,
          synopsis: anime.synopsis,
          type: anime.type,
          start_date: anime.start_date
=======
        const animes = response.data;
        this.animes = animes.map((anime) => ({
          title: anime.title,
          image: anime.image,
          synopsis: anime.synopsis,
          type: anime.type,
          start_date: false
>>>>>>> 82ae232e71606912b38e247e84aba32f5eeed0af
            ? new Date(anime.start_date).toLocaleDateString('en-US', {
              year: 'numeric',
              month: 'short',
            })
            : null,
<<<<<<< HEAD
          end_date: anime.end_date
=======
          end_date: false
>>>>>>> 82ae232e71606912b38e247e84aba32f5eeed0af
            ? new Date(anime.end_date).toLocaleDateString('en-US', {
              year: 'numeric',
              month: 'short',
            })
            : null,
          score: anime.score,
          rating: anime.rating,
          airing: anime.airing,
<<<<<<< HEAD
          id: anime.id,
        }));
      } catch (err) {
        console.log(err);
=======
          id: anime.mal_id,
        }));
      } catch (err) {
>>>>>>> 82ae232e71606912b38e247e84aba32f5eeed0af
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
<<<<<<< HEAD
        console.log(this.searchQuery);
=======
>>>>>>> 82ae232e71606912b38e247e84aba32f5eeed0af
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
