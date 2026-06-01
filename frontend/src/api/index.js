import http from './http'

export const login = (data) => http.post('/login', data)
export const me = () => http.get('/users/me')

export const listActivities = (params) => http.get('/activities', { params })
export const getActivity = (id) => http.get(`/activities/${id}`)
export const createActivity = (data) => http.post('/activities', data)
export const updateActivity = (id, data) => http.put(`/activities/${id}`, data)
export const publishActivity = (id) => http.put(`/activities/${id}/publish`)
export const cancelActivity = (id) => http.put(`/activities/${id}/cancel`)

export const registerActivity = (activityId) => http.post('/registrations', { activityId })
export const cancelRegistration = (id) => http.put(`/registrations/${id}/cancel`)
export const myRegistrations = () => http.get('/registrations/mine')
export const activityRegistrations = (id, params) => http.get(`/activities/${id}/registrations`, { params })

export const generateCheckinCode = (activityId) => http.post('/checkins/code', { activityId })
export const submitCheckin = (data) => http.post('/checkins', data)
export const manualCheckin = (data) => http.post('/checkins/manual', data)
export const submitRating = (data) => http.post('/ratings', data)
export const activityStatistics = (id) => http.get(`/statistics/activity/${id}`)
export const recommendations = () => http.get('/recommendations')

export const adminUsers = () => http.get('/admin/users')
export const updateUserStatus = (type, id, status) => http.put(`/admin/users/${type}/${id}/status`, { status })
export const auditActivity = (id, auditStatus) => http.put(`/admin/activities/${id}/audit`, { auditStatus })
