# Paper-Manage-System
our class design for software engineering

This is our class design in hit software engineering class.

Our project is to design a paper manage system.

connection test successfully, Let's start now!

首先，我们确定一下当前项目的文件结构 
```
D:.
├─docs
│  └─graph
└─src //存放前端vue的资源
    ├─assets   //存放所用图标
    ├─components //存放vue组件
    ├─pages   //存放设计好的vue界面
    ├─App.vue   //入口vue
    ├─main.js  
    ├─index.html
    ├─route.js   //管理路由
    
```
pages中当前已经存入的界面有
- Home.vue 主界面
- MyNotices 我的消息界面
- ManageTeamArticles 团队参考论文界面 

components中存放封装好的vue组件
- bar.vue 导航栏

距离第一次迭代还需要完成
- [ ] #查询结果界面
- [ ] #我的团队界面

将vueProject文件夹当作一个vue项目，先npm install,再npm run dev运行