import { createRouter,createWebHistory } from "vue-router";
import MyNotices from "./pages/MyNotices.vue";
import Home from "./pages/Home.vue"
import ManageTeamArticle from "./pages/ManageTeamArticle.vue"
import SearchResult from "./pages/SearchResult.vue"
import MyTeam from "./pages/MyTeam.vue"
import LoginView from "./pages/LoginView.vue"
import MyPaper from "./pages/MyPaper.vue"

const routes=[
    {path:'/',component:Home},
    {path:'/notice',component:MyNotices},
    {path:'/search',component:SearchResult},
    {path:'/myteam',component:MyTeam},
    {path:'/other',component:ManageTeamArticle},
    {path:'/login',component:LoginView},
    {path:'/mypaper',component:MyPaper}
]
const router = createRouter({
    history:createWebHistory(),
    routes,
})
export default router