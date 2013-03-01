//
//  WebViewController.m
//  TwinRinksAdultHockey
//
//  Created by Andrew on 1/5/13.
//  Copyright (c) 2013 GigaStorm. All rights reserved.
//

#import "WebViewController.h"

@implementation WebViewController

- (void)viewDidLoad
{
    _myWebView.scrollView.bounces = NO;
    [super viewDidLoad];
    
    [self initButtons];
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    
    BOOL isOn = [defaults boolForKey:@"IsAutoLoginOn"];
    NSString *username = [defaults stringForKey:@"SavedUsername"];
    NSString *password = [defaults stringForKey:@"SavedPassword"];
    
    NSString *urlString = @"http://www.twinrinks.com/adulthockey/subs/subs_entry.php?subs_data1=";
    urlString = [urlString stringByAppendingString:username];
    urlString = [urlString stringByAppendingString:@"&subs_data2="];
    urlString = [urlString stringByAppendingString:password];
    
    NSString *offUrlString = @"http://www.twinrinks.com/adulthockey/subs/subs_entry.php";
    
    NSURL *url;
    if(isOn)
        url = [NSURL URLWithString:urlString];
    else
        url = [NSURL URLWithString:offUrlString];
    NSURLRequest *requestObj = [NSURLRequest requestWithURL:url];
    [_myWebView loadRequest:requestObj];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

-(IBAction)doNothing:(id)sender
{
    return;
}

- (IBAction)settingsButtonPressed:(id)sender
{
    UIViewController *settingsViewController = [[self storyboard] instantiateViewControllerWithIdentifier:@"SettingsNavController"];
    settingsViewController.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
    [self presentViewController:settingsViewController animated:YES completion:nil];
}

- (IBAction)helpButtonPressed:(id)sender
{
    UIViewController *helpViewController = [[self storyboard] instantiateViewControllerWithIdentifier:@"HelpNavController"];
    helpViewController.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
    [self presentViewController:helpViewController animated:YES completion:nil];
}

-(void)initButtons {
    UIBarButtonItem *help = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"help.png"] style:UIBarButtonItemStylePlain target:self action:@selector(helpButtonPressed:)];
    UIBarButtonItem *settings = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"gear.png"] style:UIBarButtonItemStylePlain target:self action:@selector(settingsButtonPressed:)];
    UIBarButtonItem *refresh = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"reload.png"] style:UIBarButtonItemStylePlain target:self action:@selector(doNothing:)];
    
    NSArray *buttons = [[NSMutableArray alloc] initWithObjects:settings,help,nil];
    self.navigationItem.rightBarButtonItems = buttons;
    self.navigationItem.leftBarButtonItem = refresh;
}

@end