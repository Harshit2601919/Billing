// ============================================================
// BillFlow API Utility
// ============================================================
const API_BASE_URL = '/api';

function getToken() {
    return localStorage.getItem('jwt_token');
}

function setToken(token) {
    localStorage.setItem('jwt_token', token);
}

function clearToken() {
    localStorage.removeItem('jwt_token');
}

function handleLogout() {
    clearToken();
    window.location.href = '/01-login-signin.html';
}

// Require authentication — call this at the top of every PROTECTED page
function requireAuth() {
    if (!getToken()) {
        window.location.href = '01-login-signin.html';
        return false;
    }
    return true;
}

/**
 * Central fetch wrapper with JWT. Used for all PROTECTED API calls.
 * On 401/403: silently clears token and redirects (no alert popup).
 */
async function apiFetch(endpoint, options = {}) {
    const token = getToken();

    if (!token) {
        window.location.href = '01-login-signin.html';
        throw new Error('Not authenticated');
    }

    const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
        ...(options.headers || {})
    };

    let response;
    try {
        response = await fetch(`${API_BASE_URL}${endpoint}`, {
            ...options,
            headers
        });
    } catch (networkError) {
        throw new Error('Cannot connect to server. Is the backend running on port 8081?');
    }

    // Session expired / unauthorized → silent redirect, no popup
    if (response.status === 401 || response.status === 403) {
        clearToken();
        window.location.replace('01-login-signin.html');
        throw new Error('Session expired');
    }

    // PDF / binary response
    const contentType = response.headers.get('content-type') || '';
    if (contentType.includes('application/pdf') || contentType.includes('octet-stream')) {
        if (!response.ok) throw new Error('Failed to download file');
        const blob = await response.blob();
        return { isBlob: true, data: blob };
    }

    // JSON or text response
    const text = await response.text();
    let data;
    try {
        data = text ? JSON.parse(text) : null;
    } catch {
        data = text;
    }

    if (!response.ok) {
        const msg = (data && data.message) ? data.message
                  : (typeof data === 'string' && data.length < 200) ? data
                  : `Request failed with status ${response.status}`;
        throw new Error(msg);
    }

    return data;
}

/**
 * Auth-only fetch — NO JWT, NO redirects. Used strictly for login/signup.
 */
async function authFetch(endpoint, body) {
    let response;
    try {
        response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });
    } catch (networkError) {
        throw new Error('Cannot connect to server. Is the backend running on port 8081?');
    }

    const text = await response.text();
    let data;
    try {
        data = text ? JSON.parse(text) : null;
    } catch {
        data = text;
    }

    if (!response.ok) {
        const msg = (data && data.message) ? data.message
                  : (typeof data === 'string' && data.length < 200) ? data
                  : `Error ${response.status}`;
        throw new Error(msg);
    }

    return data;
}

/**
 * Common App Initialization:
 * Fetches current user profile and updates UI elements like "Welcome back, [Name]"
 * Automatically called on DOMContentLoaded for protected pages.
 */
async function initializeUserInfo() {
    if (!getToken()) return;

    try {
        const user = await apiFetch('/auth/me');
        if (user) {
            window.currentUser = user; 

            // Update Username elements
            const userNameElements = document.querySelectorAll('.user-name-text, header p.font-semibold, aside .text-white.font-bold.text-sm');
            userNameElements.forEach(el => {
                if (el.textContent.includes('Welcome back')) {
                    el.textContent = `Welcome back, ${user.username}`;
                } else if (el.tagName === 'P' || el.tagName === 'SPAN' || el.tagName === 'DIV') {
                    el.textContent = user.username;
                }
            });

            // Update Company Name elements
            const companyElements = document.querySelectorAll('.user-company-text');
            companyElements.forEach(el => {
                el.textContent = user.companyName || 'Business Owner';
            });
            
            // Update Sidebar Logo/Header if it uses the company name
            const brandSubtext = document.querySelector('aside p.text-[10px]');
            if (brandSubtext && user.companyName) {
                brandSubtext.textContent = user.companyName;
            }
        }
    } catch (err) {
        console.error('Failed to initialize user info:', err);
    }
}

// Auto-run on protected pages
if (getToken() && !window.location.pathname.includes('login') && !window.location.pathname.includes('register')) {
    document.addEventListener('DOMContentLoaded', initializeUserInfo);
}
