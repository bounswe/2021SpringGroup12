<template>
  <div class="anime-card" @click="$emit('clicked', anime.id)">
    <div class="info">
      <div class="type">
        <span>{{ anime.type }}</span>
      </div>
      <div class="airing" v-if="anime.airing">Airing</div>
      <div class="airing" v-else-if="!anime.airing && !anime.start_date">
        Planned
      </div>
      <div class="airing" v-else>Finished</div>
      <div class="score">
        <span>★{{ anime.score }}</span>
      </div>
    </div>
    <div class="title">
      <h3>{{ anime.title }}</h3>
      <div>
        <span v-if="anime.start_date">{{ anime.start_date }}</span>
        <span v-else>TBA</span>-<span v-if="anime.end_date">{{
          anime.end_date
        }}</span
        ><span v-else>TBA</span>
      </div>
    </div>
    <img :src="anime.image" :alt="anime.title" />
    <p>{{ anime.synopsis }}</p>
  </div>
</template>

<script>
export default {
  props: {
    anime: {
      type: Object,
      required: true,
    },
  },
};
</script>

<style>
.anime-card {
  border: 2px solid black;
  border-radius: 5px;
  width: 40ch;
  cursor: pointer;
}
.title {
  height: 6em;
}
.title > p {
  font-size: 0.8em;
}
img {
  max-height: 50%;
  max-width: auto;
}
.info {
  display: flex;
  justify-content: space-between;
  margin: 5px;
  text-align: center;
}
.type,
.airing,
.score {
  font-size: 1em;
  flex: 1;
}
.type {
  text-align: left;
}
.score {
  text-align: right;
}
.type > span {
  margin-right: auto;
}
.score > span {
  margin-left: auto;
}
</style>
