
import React from 'react'
import { Metadata } from 'next';
import SearchProductPageView from './SearchProductPageView';
import { clearSelectedLoan } from '@/services/loans/loan.actions';

export const metadata: Metadata = {
  title: "Buscar producto",
};
export default function page() {

  clearSelectedLoan();
  
  return (
   <SearchProductPageView/>
  )
}
