import { CSSProperties } from 'react';

declare global {
  namespace JSX {
    interface IntrinsicElements {
      'lord-icon': {
        src?: string;
        trigger?: 'hover' | 'click' | 'loop' | 'morph' | 'boomerang';
        colors?: string;
        delay?: string | number;
        style?: CSSProperties;
        state?: string;
        loading?: 'lazy' | 'eager';
        class?: string;
      };
    }
  }
}
