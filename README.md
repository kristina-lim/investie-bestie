InvestieBestie

Overview of the Project

InvestieBestie is an innovative tool designed to make stock market insights accessible to individuals with no prior experience or education in investing. The application solves the problem of understanding stock volatility by calculating the Beta rating of selected stocks and providing a concise, user-friendly analysis. Using the power of generative AI, InvestieBestie generates five bullet points summarizing the significance of the Beta rating, empowering users to make informed financial decisions.

Target Audience

The primary audience for InvestieBestie includes:

	•	Beginner investors.
	•	Individuals looking to understand stock performance without diving into technical jargon.
	•	People interested in exploring the stock market but unsure where to start.

Future Plans

After the class, the project has the potential to:

	•	Expand to include visualizations of stock trends, such as moving averages and historical data charts.
	•	Integrate personalized recommendations based on user preferences and portfolio goals.
	•	Provide additional financial metrics such as P/E ratios, dividend yields, or moving average crossovers.
	•	Explore mobile app development for better accessibility.

Database Description

InvestieBestie uses a simple database structure to store stock-related data. The database includes a single table with the following fields:

	•	stockId: A unique identifier for each stock entry (primary key).
	•	tickerSymbol: The ticker symbol of the stock (e.g., AAPL for Apple, TSLA for Tesla).
	•	betaValue: The calculated Beta rating for the stock.

This streamlined approach ensures efficient storage and retrieval of data for analysis.

AI Description

InvestieBestie incorporates Generative AI to transform raw financial data into accessible insights. The AI is used in the following way:

	•	Beta Rating Analysis: After calculating the Beta rating for a stock, the application sends this information to OpenAI’s API.
	•	Natural Language Output: The AI generates five bullet points explaining the significance of the Beta rating, tailored for beginners. The output includes insights into the stock’s volatility, risk level, and market performance context.

This integration of AI ensures that complex financial metrics are converted into actionable knowledge, making investing less intimidating for newcomers.

Running the Program:

1. Use your preferred IDE to open the repo (preferably VS Code. The back end of this project is located in investie-bestie/investie-bestie.
2. Go to OpenAI console and generate your own API key as the current key was deactivated.
3. Go to the app.yaml file located in src/main/appengine/app.yaml and paste the api key in there.
4. Open your terminal and navigate to investie-bestie/investie-bestie. Run gcloud app deploy.
5. After it deploys navigate to the front end and run npm start in the terminal. Make sure to install any necessary libraries that notifies you about.
6. Log in and enjoy!
