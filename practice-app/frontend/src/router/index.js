import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue';
import SearchResults from '../anime/views/SearchResults.vue';
import Detail from '../anime/views/Detail.vue';
import AnimeHome from '../anime/views/AnimeHome.vue';
import AnimePost from '../anime/views/Post.vue';
import BooksPostHome from '../books/pages/PostBook.vue';
import BooksGetHome from '../books/pages/GetBook.vue';
import BooksHome from '../books/pages/BooksHome.vue';
import NameInfoHome from '../nameinfo/pages/Home.vue';
import NameInfoGet from '../nameinfo/pages/GetNameInfo.vue';
import NameInfoPost from '../nameinfo/pages/PostNameInfo.vue';

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
  },
  {
    path: '/getbooks',
    name: 'BooksGetHome',
    component: BooksGetHome,
  },
  {
    path: '/postbooks',
    name: 'BooksPostHome',
    component: BooksPostHome,
  },
  {
    path: '/books',
    name: 'Bookshome',
    component: BooksHome,
  },
  {
    path: '/getnameinfo',
    name: 'NameInfoGet',
    component: NameInfoGet,
  },
  {
    path: '/postnameinfo',
    name: 'NameInfoPost',
    component: NameInfoPost,
  },
  {
    path: '/homenameinfo',
    name: 'NameInfoHome',
    component: NameInfoHome,
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
    component: AnimePost,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
