import React from 'react'

export   const CustomInput = ({value}:{value:string|null}) => {
    return <input type="text" disabled value={value !=null? value:''} />}

