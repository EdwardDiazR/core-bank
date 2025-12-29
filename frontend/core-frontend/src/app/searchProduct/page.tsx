import React from 'react'
import { Metadata } from 'next';
import SearchProductPageView from './SearchProductPageView';

export const metadata: Metadata = {
  title: "Buscar producto",
};
export default function page() {
  return (
   <SearchProductPageView/>
  )
}
