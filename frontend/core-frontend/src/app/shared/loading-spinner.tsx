import React from 'react'

export const LoadingSpinner = () => {
  return (
   <div className="flex flex-1 h-full justify-center items-center">
          <div role="status">
            <div className="flex items-center justify-center h-screen">
              <div className="h-12 w-12 animate-spin rounded-full border-4 border-solid border-green-500 border-t-transparent"></div>
            </div>
          </div>
        </div>
  )
}
