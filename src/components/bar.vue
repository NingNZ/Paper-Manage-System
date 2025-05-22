<template>
    <div id="bar">
        <header class="navbar">
      <div class="logo">
        <div class="content"><img src="./assets/take-off.svg" alt="logo" class="logo-icon" /><span style="height: 32px;padding: 0;">Paper</span><br>Manage</div>
      </div>
      <nav class="menu">
        <a href="#">首页</a>
        <a href="#">我的团队</a>
        <a href="#">我的论文</a>
        <a href="#">我的消息</a>
      </nav>
      <div class="user-icon">
        <img src="https://img.icons8.com/ios-filled/50/user-male-circle.png" alt="user" />
      </div>
    </header>
    </div>
</template>
<style>
.navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #3398ff;
  color: white;
  padding: 0.5rem 2rem;
}

.logo {
  font-weight: bold;
  font-size: 1rem;
  line-height: 1.2;
  display: flex;
  align-items: center;
  gap: 8px;
}
.logo > .content{
    font-family:'Times New Roman', Times, serif;
    font-size:larger;
    font-style: italic;
    line-height:100%;
    padding: 0%;
}
.logo-icon {
  width: 32px;
  height: 32px;
}
.menu a {
  color: white;
  margin: 0 1rem;
  text-decoration: none;
  padding: 12px 16px;
  border-radius: 4px;
  transition: background 0.3s ease, transform 0.2s ease;
  display: inline-block;
}

.menu a:hover {
  background: linear-gradient(45deg, #6fb1fc, #1c92f2);
  transform: scale(1.05);
}

.user-icon img {
  width: 50px;
  height: 50px;
}
</style>