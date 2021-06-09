import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue';
import SearchResults from '../anime/views/SearchResults.vue';
import Detail from '../anime/views/Detail.vue';
import AnimeHome from '../anime/views/AnimeHome.vue';
import AnimePost from '../anime/views/Post.vue';
import BooksPostHome from '../books/pages/PostBook.vue';
import BooksGetHome from '../books/pages/GetBook.vue';
import BooksHome from '../books/pages/BooksHome.vue';
import Movies from '../movies/pages/movie_home.vue';
import MoviesReview from '../movies/pages/add_review.vue';
import MoviesKeyword from '../movies/pages/keyword2.vue';
import Post from '../anime/views/Post.vue';
import BooksPostHome from '../books/pages/PostBook.vue';
import BooksGetHome from '../books/pages/GetBook.vue';
import BooksHome from '../books/pages/BooksHome.vue';
import PostIssue from '../issues/pages/PostIssue.vue';
import GetAll from '../issues/pages/GetAll.vue';
import GetIssue from '../issues/pages/GetIssue.vue';
import HomeIssue from '../issues/pages/IssuesHome.vue';
import ConvertHome from '../convert/views/ConvertHome.vue';

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
    {
    path: '/getall',
    name: 'GetAll',
    component: GetAll,
  },
  {
    path: '/getissue',
    name: 'GetIssue',
    component: GetIssue,
  },
  {
    path: '/postissues',
    name: 'PostIssue',
    component: PostIssue,
  },
  {
    path: '/issues',
    name: 'HomeIssue',
    component: HomeIssue,
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
    path: '/movies_home/',
    name: 'MoviesHome',
    component: Movies,
  },
  {
    path: '/movies/',
    name: 'MoviesKeyword',
    component: MoviesKeyword,
  },
  {
    path: '/movies_addReview/',
    name: 'MoviesReview',
    component: MoviesReview,
    component: Post,
  },
  {
    path: '/convert',
    name: 'ConvertHome',
    component: ConvertHome
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
