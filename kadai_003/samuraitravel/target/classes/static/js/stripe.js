//const stripe = Stripe('sk_test_51R7pH2CkhpTGubr37Cf3daupthDzXKsNYPETE8AIh9RPjmrMBud4LBmQ94nNlWXseuo3KChdF6KusGsgeF6pDPe300fX2eo2qk');
const stripe = Stripe('pk_test_51R7pH2CkhpTGubr3lghM62v5rWM7k5t8W9Sz7qRiLlGJcqVk9J8A7HSPTi9QsH2WwiP01hXyGC1Mhjzl5Q8IgzEH00HQ0kyVhv');
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
 stripe.redirectToCheckout({
   sessionId: sessionId
 })
});