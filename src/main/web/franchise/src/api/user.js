import request from '@/utils/request'


export function userList() {
  return request({
    url: '/user/userList',
    method: 'get',
    params: { }
  })
}

export function roleList() {
  return request({
    url: '/user/roleList',
    method: 'get',
    params: { }
  })
}

export function userAdd(user) {
  return request({
    url: '/user/userAdd',
    method: 'get',
    params: user 

  })
}

export function userUpdate(user) {
  return request({
    url: '/user/userUpdate',
    method: 'get',
    params: user

  })
}

export function userDel(id) {
  return request({
    url: '/user/userDel',
    method: 'get',
    params: { id }
  })
}
 