import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue';
/* ANIME */
import SearchResults from '../anime/views/SearchResults.vue';
import Detail from '../anime/views/Detail.vue';
import AnimeHome from '../anime/views/AnimeHome.vue';
import AnimePost from '../anime/views/Post.vue';
/* BOOKS */
import BooksPostHome from '../books/pages/PostBook.vue';
import BooksGetHome from '../books/pages/GetBook.vue';
import BooksHome from '../books/pages/BooksHome.vue';
/* NAME-INFO */
import NameInfoHome from '../nameinfo/pages/Home.vue';
import NameInfoGet from '../nameinfo/pages/GetNameInfo.vue';
import NameInfoPost from '../nameinfo/pages/PostNameInfo.vue';
/* MOVIES */
import Movies from '../movies/pages/movie_home.vue';
import MoviesReview from '../movies/pages/add_review.vue';
import MoviesKeyword from '../movies/pages/keyword2.vue';
/* ISSUES */
import PostIssue from '../issues/pages/PostIssue.vue';
import GetAll from '../issues/pages/GetAll.vue';
import GetIssue from '../issues/pages/GetIssue.vue';
import HomeIssue from '../issues/pages/IssuesHome.vue';
/* CONVERT */
import ConvertHome from '../convert/views/ConvertHome.vue';
/* QUOTE */
import QuoteHome from '../quotes/pages/QuoteHome.vue';
import PostQuote from '../quotes/pages/PostQuote.vue';
import GetQuote from '../quotes/pages/GetQuote.vue';
import RandomQuote from '../quotes/pages/RandomQuote.vue';
/* COCKTAIL */
import CocktailHome from '../cocktails/pages/CocktailHome.vue'
import PostCocktail from '../cocktails/pages/PostCocktail.vue';
import GetCocktails from '../cocktails/pages/GetCocktails.vue';

const routes = [
  /* general */
  {
    path: '/',
    name: 'Home',
    component: Home,
  },
  /* books */
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
  /* issues */
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
  /* name-age */
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
  /* anime */
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
  /* movies */
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
  },
  /* convert */
  {
    path: '/convert',
    name: 'ConvertHome',
    component: ConvertHome
  },
  /* quote */
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
  /* cocktail */
  {
    path: '/postcocktail',
    name: 'PostCocktail',
    component: PostCocktail,
  },
  {
    path: '/getcocktails',
    name: 'GetCocktails',
    component: GetCocktails,
  },
  {
    path: '/cocktails',
    name: 'CocktailHome',
    component: CocktailHome,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
