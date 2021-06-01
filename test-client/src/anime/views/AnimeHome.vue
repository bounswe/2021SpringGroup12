<template>
  <link
    rel="stylesheet"
    href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"
  />
  <p class="error" v-if="this.error !==''">{{ this.error }}</p>
  <form id="searchBar" @submit.prevent="search">
    <input type="text" v-model="searchQuery" />
    <button type="submit"><i class="fa fa-search"></i></button>
  </form>
  <div class="anime">
    <p>To use the AnimeAPI enter your query in the text input and submit.</p>
    <p>Or to post a new anime:</p>
    <button @click="postAnime">Post a new anime</button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      searchQuery: '',
      date: null,
      error: '',
    };
  },
  methods: {
    search() {
      if (this.searchQuery.length >= 3) {
        console.log(this.searchQuery);
        this.$router.push({ name: 'Search', params: { query: this.searchQuery } });
      }

      if (this.searchQuery.length < 3) {
        this.error = 'Search query must be at least 3 characters long!';
      }
    },
    postAnime() {
      this.$router.push({ name: 'Post' });
    },
  },
};
</script>
