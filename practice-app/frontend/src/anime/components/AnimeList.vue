<template>
  <div class="anime-list">
    <AnimeCard
      @clicked="clicked"
      v-for="(anime, index) in animes"
      :key="index"
      :anime="anime"
    />
  </div>
</template>

<script>
import AnimeCard from './AnimeSearchCard.vue';

export default {
  components: {
    AnimeCard,
  },
  props: {
    animes: {
      type: Array,
      required: true,
    },
  },
  methods: {
    clicked(value) {
      this.$emit('clicked', value);
    },
  },
};
</script>

<style>
.anime-list {
  display: flex;
  margin: 30px;
  justify-content: center;
  flex-wrap: wrap;
}
.anime-card {
  margin: 30px;
}
</style>
