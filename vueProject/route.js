import { createRouter,createWebHistory } from "vue-router";
import MyNotices from "./pages/MyNotices.vue";
import Home from "./pages/Home.vue"
import ManageTeamArticle from "./pages/ManageTeamArticle.vue"
import SearchResult from "./pages/SearchResult.vue"

const routes=[
    {path:'/',component:Home},
    {path:'/notice',component:MyNotices},
    {path:'/search',component:SearchResult},
    {path:'/other',component:ManageTeamArticle}
]
const router = createRouter({
    history:createWebHistory(),
    routes,
})
export default router