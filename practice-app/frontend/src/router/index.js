import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue';
import SearchResults from '../anime/views/SearchResults.vue';
import Detail from '../anime/views/Detail.vue';
import AnimeHome from '../anime/views/AnimeHome.vue';
import Post from '../anime/views/Post.vue';

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
  },
  {
    path: '/anime',
    name: 'Animehome',
    component: AnimeHome,
  },
  {
    path: '/anime/search/:query',
    name: 'Search',
    component: SearchResults,
  },
  {
    path: '/anime/:id',
    name: 'Anime',
    component: Detail,
  },
  {
    path: '/anime/post',
    name: 'Post',
    component: Post,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
