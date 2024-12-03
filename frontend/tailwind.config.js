/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: '#010F18',
        secondary1: '#FF9944',
        secondary2: '#C8E1E8',
        secondary2Hover: '#A0BCE0',
        text: '#FFFFFF',
      },
    },
  },
  plugins: [],
}