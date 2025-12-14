<template>
  <div class="dashboard">
    <div class="welcome-card">
      <div class="welcome-header">
        <div>
          <h2>欢迎回来，{{ auth.username }}</h2>
          <p class="role-tag">{{ getRoleText(auth.role) }}</p>
        </div>
        <el-icon :size="48" color="#409eff"><UserFilled /></el-icon>
      </div>
    </div>

    <!-- 管理员视图 -->
    <template v-if="auth.role === 'ADMIN'">
      <div class="info-card">
        <h3>📚 学生管理系统</h3>
        <div class="system-summary">
          <p class="intro">基于 <strong>Spring Boot 3 + Vue 3 + MySQL 8</strong> 的现代化教务管理平台，支持多角色权限管理和完整的教学业务流程。</p>
          
          <div class="section">
            <h4>🎯 核心功能</h4>
            <div class="feature-grid">
              <div class="feature-item">
                <el-icon :size="20" color="#409eff"><User /></el-icon>
                <div>
                  <strong>用户管理</strong>
                  <p>支持学生、教师、管理员三种角色，基于JWT的安全认证</p>
                </div>
              </div>
              <div class="feature-item">
                <el-icon :size="20" color="#67c23a"><Reading /></el-icon>
                <div>
                  <strong>双课程体系</strong>
                  <p>正课全员可见,选课支持选课和人数限制</p>
                </div>
              </div>
              <div class="feature-item">
                <el-icon :size="20" color="#e6a23c"><EditPen /></el-icon>
                <div>
                  <strong>双成绩管理</strong>
                  <p>选课成绩基于选课记录，正课成绩直接关联课程</p>
                </div>
              </div>
              <div class="feature-item">
                <el-icon :size="20" color="#f56c6c"><Document /></el-icon>
                <div>
                  <strong>组织架构</strong>
                  <p>院系-专业-班级三级管理，动态下拉框数据联动</p>
                </div>
              </div>
            </div>
          </div>

          <div class="section">
            <h4>💡 技术特色</h4>
            <div class="tech-tags">
              <el-tag type="primary">Spring Security + JWT</el-tag>
              <el-tag type="success">MyBatis + MySQL</el-tag>
              <el-tag type="warning">Vue 3 + TypeScript</el-tag>
              <el-tag type="danger">Element Plus</el-tag>
              <el-tag type="info">RESTful API</el-tag>
              <el-tag>Pinia 状态管理</el-tag>
            </div>
          </div>

          <div class="section">
            <h4>👥 权限说明</h4>
            <div class="role-list">
              <div class="role-item admin">
                <strong>管理员 (ADMIN)</strong>
                <p>拥有系统全部权限：用户管理、组织架构、课程管理、成绩录入</p>
              </div>
              <div class="role-item teacher">
                <strong>教师 (TEACHER)</strong>
                <p>查看学生信息、管理自己的课程、录入自己课程的成绩</p>
              </div>
              <div class="role-item student">
                <strong>学生 (STUDENT)</strong>
                <p>浏览课程信息、选课退课操作、查看个人成绩</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </template>

    <!-- 教师视图 -->
    <template v-else-if="auth.role === 'TEACHER'">
      <div class="info-card">
        <h3>👨‍🏫 教师工作台</h3>
        <div class="system-summary">
          <p class="intro">欢迎使用学生管理系统！您可以管理自己的课程、查看学生信息并录入成绩。</p>
          
          <div class="section">
            <h4>📖 主要功能</h4>
            <div class="feature-grid">
              <div class="feature-item">
                <el-icon :size="20" color="#409eff"><Reading /></el-icon>
                <div>
                  <strong>课程管理</strong>
                  <p>查看和管理您教授的正课和选课</p>
                </div>
              </div>
              <div class="feature-item">
                <el-icon :size="20" color="#67c23a"><EditPen /></el-icon>
                <div>
                  <strong>成绩录入</strong>
                  <p>为您的课程录入和修改学生成绩</p>
                </div>
              </div>
              <div class="feature-item">
                <el-icon :size="20" color="#e6a23c"><User /></el-icon>
                <div>
                  <strong>学生管理</strong>
                  <p>查看学生信息和选课情况</p>
                </div>
              </div>
            </div>
          </div>

          <div class="quick-links">
            <h4>🔗 快速访问</h4>
            <p>使用左侧导航栏访问：<strong>学生</strong> → <strong>课程</strong> → <strong>成绩</strong></p>
          </div>
        </div>
      </div>
    </template>

    <!-- 学生视图 -->
    <template v-else-if="auth.role === 'STUDENT'">
      <div class="info-card">
        <h3>👨‍🎓 学生学习中心</h3>
        <div class="system-summary">
          <p class="intro">欢迎使用学生管理系统！您可以浏览课程、选课退课并查看个人成绩。</p>
          
          <div class="section">
            <h4>📚 主要功能</h4>
            <div class="feature-grid">
              <div class="feature-item">
                <el-icon :size="20" color="#409eff"><Reading /></el-icon>
                <div>
                  <strong>浏览课程</strong>
                  <p>查看所有正课和您已选的选课</p>
                </div>
              </div>
              <div class="feature-item">
                <el-icon :size="20" color="#67c23a"><EditPen /></el-icon>
                <div>
                  <strong>选课管理</strong>
                  <p>选择感兴趣的课程，也可以退选已选课程</p>
                </div>
              </div>
              <div class="feature-item">
                <el-icon :size="20" color="#e6a23c"><Trophy /></el-icon>
                <div>
                  <strong>成绩查询</strong>
                  <p>查看您的选课成绩和正课成绩</p>
                </div>
              </div>
            </div>
          </div>

          <div class="quick-links">
            <h4>🔗 快速访问</h4>
            <p>使用左侧导航栏访问：<strong>课程</strong> → <strong>选课</strong> → <strong>成绩</strong></p>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/auth';
import { UserFilled, User, Reading, Document, EditPen, Trophy } from '@element-plus/icons-vue';

const auth = useAuthStore();

const getRoleText = (role: string | null) => {
  const roleMap: { [key: string]: string } = {
    'ADMIN': '系统管理员',
    'TEACHER': '教师',
    'STUDENT': '学生'
  };
  return role ? roleMap[role] : '未知角色';
};
</script>

<style scoped>
.dashboard {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.welcome-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 32px;
  border-radius: 12px;
  color: white;
  box-shadow: 0 8px 24px rgba(102, 126, 234, 0.3);
}

.welcome-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-header h2 {
  margin: 0;
  font-size: 28px;
  font-weight: 600;
}

.role-tag {
  margin: 8px 0 0 0;
  font-size: 14px;
  opacity: 0.9;
}



.info-card {
  background: #fff;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}
.system-summary .intro {
  font-size: 15px;
  color: #606266;
  line-height: 1.8;
  margin: 0 0 24px 0;
}

.section {
  margin-bottom: 24px;
}

.section h4 {
  font-size: 16px;
  color: #303133;
  margin: 0 0 16px 0;
  padding-left: 12px;
  border-left: 3px solid #409eff;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 16px;
}

.feature-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 16px;
  background: #f5f7fa;
  border-radius: 8px;
  transition: all 0.3s;
}

.feature-item:hover {
  background: #ecf5ff;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.1);
}

.feature-item > div {
  flex: 1;
}

.feature-item strong {
  display: block;
  font-size: 14px;
  color: #303133;
  margin-bottom: 4px;
}

.feature-item p {
  margin: 0;
  font-size: 13px;
  color: #909399;
  line-height: 1.6;
}

.tech-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.tech-tags .el-tag {
  font-size: 13px;
}

.role-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.role-item {
  padding: 16px;
  border-radius: 8px;
  border-left: 4px solid;
}

.role-item.admin {
  background: #fef0f0;
  border-color: #f56c6c;
}

.role-item.teacher {
  background: #f0f9ff;
  border-color: #409eff;
}

.role-item.student {
  background: #f0f9ff;
  border-color: #67c23a;
}

.role-item strong {
  display: block;
  font-size: 14px;
  color: #303133;
  margin-bottom: 6px;
}

.role-item p {
  margin: 0;
  font-size: 13px;
  color: #606266;
  line-height: 1.6;
}

.quick-links {
  margin-top: 24px;
  padding: 16px;
  background: #fef7e6;
  border-radius: 8px;
  border-left: 4px solid #e6a23c;
}

.quick-links h4 {
  font-size: 14px;
  color: #303133;
  margin: 0 0 8px 0;
}

.quick-links p {
  margin: 0;
  font-size: 13px;
  color: #606266;
  line-height: 1.6;
}

.quick-links strong {
  color: #e6a23c;
}
</style>
