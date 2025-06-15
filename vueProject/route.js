import { createRouter,createWebHistory } from "vue-router";
import MyNotices from "./pages/MyNotices.vue";
import Home from "./pages/Home.vue"
import ManageTeamArticle from "./pages/ManageTeamArticle.vue"
import SearchResult from "./pages/SearchResult.vue"
import MyTeam from "./pages/MyTeam.vue"
import LoginView from "./pages/LoginView.vue"
import MyPaper from "./pages/MyPaper.vue"
import { sessionUtil } from './scripts/session';
import { ElMessage } from 'element-plus';

const routes=[
    {path:'/',component:Home},
    {path:'/notice',component:MyNotices,meta: { minPermission: 0 }},
    {path:'/search',component:SearchResult},
    {path:'/myteam',component:MyTeam,meta: { exactPermission: 0 }},
    {path:'/other',name: 'other',component:ManageTeamArticle},
    {path:'/login',component:LoginView},
    {path:'/mypaper',component:MyPaper,meta: { exactPermission: 0 }}
]
const router = createRouter({
    history:createWebHistory(),
    routes,
})

router.beforeEach(async (to, from, next) => {
  // 对 /search 特别限制
  if (to.path === '/search') {
    const isSearch = localStorage.getItem('isSearch');
    if (isSearch) {
      localStorage.removeItem('isSearch');
      return next();
    } else {
      return next('/');
    }
  }

  // 异步获取权限
  let permission = -1;
  try {
    permission = await sessionUtil.checkPermiss();
  } catch (e) {
    permission = -1;
  }

  // 权限控制逻辑
  const { exactPermission, minPermission, requiresAdmin } = to.meta;

  // 只有管理员权限
  if (requiresAdmin && permission !== 1) {
    return next('/');
  }

  // 只有普通用户权限（我的团队，我的论文）
  if (typeof exactPermission === 'number' && permission !== exactPermission) {
    if (permission === 1) {
      ElMessage.warning("您是管理员，无法访问此页面！");
    } else if (permission === -1) {
      ElMessage.error("请先登录后再访问该页面！");
    }
    return next('/');
  }
  // 普通用户或者管理员权限（我的消息）
  if (typeof minPermission === 'number' && permission < minPermission) {
    if (permission === -1) {
      ElMessage.error("请先登录后再访问该页面！");
    }
    return next('/');
  }

  return next();
});

export default router;