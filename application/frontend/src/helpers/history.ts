import { createBrowserHistory, LocationState } from 'history'
export default createBrowserHistory<LocationState>({
    basename:'app'
})