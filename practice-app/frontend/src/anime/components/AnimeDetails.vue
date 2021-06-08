<template>
  <div class="anime-detail">
    <div class="first-row">
      <img :src="anime.image" :alt="anime.title" />
      <div class="box">
        <div class="topinfo">
          <span>{{ anime.type }}</span
          ><span>★{{ anime.score }}</span>
        </div>
        <h1>{{ anime.title }}</h1>
        <div class="dates">
          <span v-if="anime.start_date">{{ anime.start_date }}</span>
          <span v-else>TBA</span>-<span v-if="anime.end_date">{{
            anime.end_date
          }}</span
          ><span v-else>TBA</span>
        </div>
        <div class="genre">
          <span
            class="genres"
            v-for="(genre, index) in anime.genres"
            :key="index"
            >{{ genre.name }}</span
          >
        </div>
        <div class="episodes">
          <span>{{ anime.episodes }} Episodes</span>-<span
            >{{ anime.duration }}m per Episode</span
          >
          <p>
            Duration:
            {{ Math.floor((anime.episodes * anime.duration) / 60) }} hours
            {{ (anime.episodes * anime.duration) % 60 }} minutes
          </p>
        </div>
        <div class="sequel-prequel">
          <div
            class="prequel"
            v-if="anime.prequel"
            @click="$emit('clicked-related', anime.prequel.id)"
          >
            Prequel
            <p>{{ anime.prequel.name }}</p>
          </div>
          <div class="prequel-null" v-else>
            Prequel
            <p>None</p>
          </div>
          <div
            class="sequel"
            v-if="anime.sequel"
            @click="$emit('clicked-related', anime.sequel.id)"
          >
            Sequel
            <p>{{ anime.sequel.name }}</p>
          </div>
          <div class="sequel-null" v-else>
            Sequel
            <p>None</p>
          </div>
        </div>
      </div>
    </div>
    <h2>Synopsis</h2>
    <p class="detail-synopsis">{{ anime.synopsis }}</p>
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
  data() {
    return {
      isOrderGenerated: false,
      orderObject: Object,
    };
  },
};
</script>

<style>
.anime {
  width: 70%;
  text-align: center;
  margin: auto;
  border: 2px solid black;
  border-radius: 15px;
  padding: 15px;
  background-color: hsl(113, 92%, 97%, 40%);
}
.first-row {
  margin: auto;
  display: flex;
}
.first-row > img {
  width: 25%;
  margin-right: 50px;
}
.first-row > div {
  text-align: center;
  width: 70%;
}
h1 {
  font-size: 3em;
  margin-bottom: 0px;
  margin-top: 0px;
}
.topinfo {
  display: flex;
  justify-content: space-between;
}
.topinfo > span {
  width: 7ch;
  padding: 2px;
  border: 1px solid green;
  background-color: lemonchiffon;
}
.detail-synopsis {
  margin: 15px;
  margin-top: 30px;
}
.genres {
  margin: 5px;
}
.sequel-prequel {
  display: flex;
  justify-content: space-around;
}
.genre,
.episodes,
.sequel-prequel,
.dates {
  margin-top: 1rem;
}
.sequel,
.prequel {
  border: 1px solid black;
  border-radius: 5px;
  background-color: hsl(120, 73%, 75%, 20%);
  padding: 1rem;
  width: 30%;
  cursor: pointer;
}
.sequel-null,
.prequel-null {
  border: 1px solid black;
  border-radius: 5px;
  background-color: hsl(120, 73%, 75%, 20%);
  padding: 1rem;
  width: 30%;
}
</style>
