/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      transitionDuration: {
        5000: "5000ms",
      },
      transitionProperty: {
        height: "height",
        width: "width",
      }
    },
  },
  plugins: [],
}