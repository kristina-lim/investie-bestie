import { initializeApp } from 'firebase/app';
import { getAuth } from 'firebase/auth';

const firebaseConfig = {
  apiKey: "AIzaSyCJirx4dVfHTbC6OF-zNK8ZxYKiCYgvG6I",
  authDomain: "investiebestie-bdf6e.firebaseapp.com",
  projectId: "investiebestie-bdf6e",
  storageBucket: "investiebestie-bdf6e.firebasestorage.app",
  messagingSenderId: "893441570954",
  appId: "1:893441570954:web:6d0a4d6629f1c09d161573"
};

const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);