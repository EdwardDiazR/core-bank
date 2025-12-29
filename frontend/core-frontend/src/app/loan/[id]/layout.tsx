import React from 'react'

export default function LoanDetailLayout({children}:{children:React.ReactNode}) {
  return (
   <div>
    {/* todo: hacer un outlet con tabs */}
    {children}
   </div>
  )
}