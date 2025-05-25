import { createRouter,createWebHistory } from "vue-router";
import MyNotices from "./pages/MyNotices.vue";
import Home from "./pages/Home.vue"
import ManageTeamArticle from "./pages/ManageTeamArticle.vue"
import SearchResult from "./pages/SearchResult.vue"
import MyTeam from "./pages/MyTeam.vue"
import LoginView from "./pages/LoginView.vue"

const routes=[
    {path:'/',component:Home},
    {path:'/notice',component:MyNotices},
    {path:'/search',component:SearchResult},
    {path:'/myteam',component:MyTeam},
    {path:'/other',component:ManageTeamArticle},
    {path:'/login',component:LoginView}
]
const router = createRouter({
    history:createWebHistory(),
    routes,
})

router.beforeEach((to,from,next)=>{
    if(to.path ==='/search'){    
        if(from.path==='/search') next()
        else{
            const isSearch = localStorage.getItem('isSearch')
            if(isSearch) {
                next()
                localStorage.removeItem('isSearch')
            }else next('/')
        }    
    }
    else next()
})
export default router