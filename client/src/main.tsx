import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App'
import './index.css'
import {Provider} from 'react-redux'
import {persistor, store} from './store'
import {PersistGate} from "redux-persist/integration/react";

// import function to register Swiper custom elements
import { register } from 'swiper/element/bundle';
// register Swiper custom elements
register();

ReactDOM.createRoot(document.getElementById('root') as HTMLElement).render(
    <React.StrictMode>
        <Provider store={store}>
            <PersistGate loading={null} persistor={persistor}>
                <App/>
            </PersistGate>
        </Provider>
    </React.StrictMode>,
)
