import React, { useEffect } from 'react';
import { GoogleAuthProvider, signInWithPopup } from 'firebase/auth';
import { auth } from '../Firebase/Firebase.js';
import { useNavigate } from 'react-router-dom';
import './LoginForm.css';

function LoginForm({ LoginEvent }) {
	const navigate = useNavigate(); // Initialize navigation

	// Function to handle Google login
	const signInWithGoogle = async () => {
		const provider = new GoogleAuthProvider();

		try {
				const result = await signInWithPopup(auth, provider);
				const user = result.user;
				console.log("User signed in:", user);

				LoginEvent(user); // Notify parent of logged-in user
				navigate('/stocks'); // Redirect to /stocks
		} catch (error) {
				console.error("Error during sign in:", error);
		}
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
	}, [LoginEvent, navigate]);

	return (
		<div className="login-container">
				<h1 className="login-header">Welcome to Investie Bestie</h1>
				<p className="login-subtext">Please log in to continue.</p>
				<button className="login-button" onClick={signInWithGoogle}>
						Sign in with Google
				</button>
		</div>
	);
}

export default LoginForm;