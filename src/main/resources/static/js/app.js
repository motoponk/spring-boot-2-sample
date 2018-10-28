new Vue({
    el: '#app',
    data: {
        users: [],
        user: {
            githubProfile: {}
        },
        userLoaded: false
    },
    created: function () {
        this.fetchUsers();
    },
    methods: {
        fetchUsers: function () {
            $.ajax({
                url: '/api/users',
                contentType: 'application/json'
            })
                .done(function (data) {
                    this.users = data;
                }.bind(this));
        },
        fetchUser: function (userId) {
            $.ajax({
                url: '/api/users/' + userId,
                contentType: 'application/json'
            })
                .done(function (data) {
                    this.user = data;
                    this.userLoaded = true;
                }.bind(this));
        }
    }
});
