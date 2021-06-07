import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue';
import SearchResults from '../anime/views/SearchResults.vue';
import Detail from '../anime/views/Detail.vue';
import AnimeHome from '../anime/views/AnimeHome.vue';
import AnimePost from '../anime/views/Post.vue';
import BooksPostHome from '../books/pages/PostBook.vue';
import BooksGetHome from '../books/pages/GetBook.vue';
import BooksHome from '../books/pages/BooksHome.vue';
import QuoteHome from '../quotes/pages/QuoteHome.vue';
import PostQuote from '../quotes/pages/PostQuote.vue';
import GetQuote from '../quotes/pages/GetQuote.vue';
import RandomQuote from '../quotes/pages/RandomQuote.vue';

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
    {
    path: '/getquotes',
    name: 'GetQuote',
    component: GetQuote,
  },
  {
    path: '/postquotes',
    name: 'PostQuote',
    component: PostQuote,
  },
  {
    path: '/randomquote',
    name: 'RandomQuote',
    component: RandomQuote,
  },
  {
    path: '/quotes',
    name: 'QuoteHome',
    component: QuoteHome,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
