import { initializeApp } from 'firebase/app';
import { getAuth, signInWithPopup, GoogleAuthProvider } from 'firebase/auth';
import React, {  useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

// LoginSuccessful is a function sent in by parent component
function LoginForm({LoginEvent, onLogout}) {
	const firebaseConfig = {
    apiKey: "AIzaSyD978vPpRWGaOpNA43QcNqx3JC68e21rOE",
    authDomain: "investie-bestie.firebaseapp.com",
    projectId: "investie-bestie",
    storageBucket: "investie-bestie.firebasestorage.app",
    messagingSenderId: "519605527992",
    appId: "1:519605527992:web:b79c24638eeaa3e03f5b4d"
	};

	initializeApp(firebaseConfig);
	const auth = getAuth();
	const navigate = useNavigate(); // Initialize navigation

	// Function to handle Google login
	const signInWithGoogle = async () => {
			const provider = new GoogleAuthProvider();

			try {
					const result = await signInWithPopup(auth, provider); // Use signInWithPopup for simplicity
					const user = result.user;
					console.log("User signed in:", user);
					LoginEvent(user); // Notify parent of logged-in user
					navigate('/stocks'); // Redirect to /stocks
			} catch (error) {
					console.error("Error during sign in:", error);
			}
	};

	const handleLogout = () => {
    const auth = getAuth();
    auth.signOut() // Firebase logout
        .then(() => {
            console.log("User signed out from Firebase.");
            onLogout(); // Clear the app-level state
            navigate("/login"); // Redirect to login
        })
        .catch((error) => {
            console.error("Error signing out: ", error);
        });
	};

	// Check if a user is already logged in
	useEffect(() => {
			const unsubscribe = auth.onAuthStateChanged((user) => {
					if (user) {
							console.log("User is already signed in:", user);
							LoginEvent(user); // Notify parent of logged-in user
							navigate('/stocks'); // Redirect to /stocks
					}
			});

			return () => unsubscribe(); // Cleanup the subscription
	}, [auth, LoginEvent, navigate]);

	return (
			<div>
					<h2>Login</h2>
					<button onClick={signInWithGoogle}>Sign in with Google</button>
			</div>
	);
}

export default LoginForm;