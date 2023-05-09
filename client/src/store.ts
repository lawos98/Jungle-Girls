import { configureStore } from "@reduxjs/toolkit";
import { persistStore, persistReducer,persistCombineReducers, FLUSH,
    REHYDRATE,
    PAUSE,
    PERSIST,
    PURGE,
    REGISTER,
} from "redux-persist";
import storageSession from "redux-persist/lib/storage/session";
import userReducer from "./reducers/UserReducer";

const persistConfig = {
    key: "root",
    storage: storageSession,
};

const rootReducer = persistCombineReducers(persistConfig, {
    user: userReducer,
});

const persistedReducer = persistReducer(persistConfig, rootReducer);

export const store = configureStore({
    reducer: persistedReducer,
    middleware: (getDefaultMiddleware) => getDefaultMiddleware({
        serializableCheck: {
            ignoredActions: [FLUSH, REHYDRATE, PAUSE, PERSIST, PURGE, REGISTER],
        },
    }),
});

export const persistor = persistStore(store);