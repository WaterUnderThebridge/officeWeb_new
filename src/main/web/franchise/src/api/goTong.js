import request from '@/utils/request'


export function gtList(FranAppId) {
  return request({
    url: '/gt/list',
    method: 'get',
    params: {FranAppId}
  })
}

export function gtSave(gt) {
  return request({
    url: '/gt/save',
    method: 'get',
    params: gt
  })
}

export function gtDel(id) {
  return request({
    url: '/gt/delete',
    method: 'get',
    params: { id }
  })
}

 
 
