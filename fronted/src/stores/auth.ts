import { defineStore } from 'pinia';
import { loginApi } from '@/api';

interface AuthState {
  token: string | null;
  role: string | null;
  username: string | null;
}

const STORAGE_KEY = 'student-manager-auth';

function loadState(): AuthState {
  const raw = localStorage.getItem(STORAGE_KEY);
  if (!raw) return { token: null, role: null, username: null };
  try {
    return JSON.parse(raw) as AuthState;
  } catch (e) {
    return { token: null, role: null, username: null };
  }
}

export const useAuthStore = defineStore('auth', {
  state: (): AuthState => loadState(),
  actions: {
    async login(username: string, password: string) {
      const data = await loginApi(username, password);
      this.token = data.token;
      this.role = data.role;
      this.username = username;
      this.persist();
    },
    logout() {
      this.token = null;
      this.role = null;
      this.username = null;
      this.persist();
    },
    persist() {
      localStorage.setItem(STORAGE_KEY, JSON.stringify({
        token: this.token,
        role: this.role,
        username: this.username
      }));
    }
  }
});
