const alpha3ToAlpha2 = {
  ARG: 'ar',
  AUS: 'au',
  BEL: 'be',
  BRA: 'br',
  CAN: 'ca',
  CHN: 'cn',
  DEU: 'de',
  ESP: 'es',
  FRA: 'fr',
  GBR: 'gb',
  IND: 'in',
  IRQ: 'iq',
  IRN: 'ir',
  ISR: 'il',
  ITA: 'it',
  JPN: 'jp',
  KWT: 'kw',
  POL: 'pl',
  PRT: 'pt',
  RUS: 'ru',
  SAU: 'sa',
  SYR: 'sy',
  TUR: 'tr',
  UKR: 'ua',
  USA: 'us'
}

export function getFlagUrl(alpha3Code) {
  const alpha2Code = alpha3ToAlpha2[alpha3Code?.toUpperCase()]

  if (!alpha2Code) {
    return null
  }

  return `https://flagcdn.com/w40/${alpha2Code}.png`
}
